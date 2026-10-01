import { CurrencyPipe } from '@angular/common';
import { afterNextRender, Component, signal } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { carregarColecao, gerarProximoId, salvarColecao } from '../../shared/local-storage';

interface ClasseRegistro {
  id: number;
  nome: string;
  valorDiaria: number;
}

const CHAVE_STORAGE = 'locadora:classes';

@Component({
  selector: 'app-classe',
  imports: [FormsModule, CurrencyPipe],
  templateUrl: './classe.html',
  styleUrl: './classe.css',
})
export class Classe {
  readonly classes = signal<ClasseRegistro[]>([]);
  nome = '';
  valorDiaria: number | null = null;
  busca = '';
  editandoId: number | null = null;
  mensagem = '';
  erro = '';

  constructor() {
    afterNextRender(() => this.classes.set(carregarColecao<ClasseRegistro>(CHAVE_STORAGE)));
  }

  get classesFiltradas(): ClasseRegistro[] {
    const termo = this.busca.trim().toLocaleLowerCase('pt-BR');
    return termo
      ? this.classes().filter((classe) => classe.nome.toLocaleLowerCase('pt-BR').includes(termo))
      : this.classes();
  }

  salvar(formulario: NgForm): void {
    this.limparFeedback();
    const nome = this.nome.trim();
    const valorDiaria = Number(this.valorDiaria);

    if (!nome) {
      this.erro = 'Informe o nome da classe.';
      return;
    }

    if (!Number.isFinite(valorDiaria) || valorDiaria <= 0) {
      this.erro = 'Informe um valor de diária maior que zero.';
      return;
    }

    const nomeDuplicado = this.classes().some(
      (classe) =>
        classe.id !== this.editandoId &&
        classe.nome.localeCompare(nome, 'pt-BR', { sensitivity: 'accent' }) === 0,
    );

    if (nomeDuplicado) {
      this.erro = 'Já existe uma classe cadastrada com esse nome.';
      return;
    }

    if (this.editandoId !== null) {
      this.atualizarLista(
        this.classes().map((classe) =>
          classe.id === this.editandoId ? { ...classe, nome, valorDiaria } : classe,
        ),
      );
      this.mensagem = 'Classe atualizada com sucesso.';
    } else {
      this.atualizarLista([
        ...this.classes(),
        { id: gerarProximoId(this.classes()), nome, valorDiaria },
      ]);
      this.mensagem = 'Classe cadastrada com sucesso.';
    }

    this.resetarFormulario(formulario);
  }

  editar(classe: ClasseRegistro): void {
    this.limparFeedback();
    this.editandoId = classe.id;
    this.nome = classe.nome;
    this.valorDiaria = classe.valorDiaria;
  }

  excluir(classe: ClasseRegistro): void {
    if (!confirm(`Deseja excluir a classe "${classe.nome}"?`)) {
      return;
    }

    this.atualizarLista(this.classes().filter((item) => item.id !== classe.id));
    if (this.editandoId === classe.id) {
      this.editandoId = null;
      this.nome = '';
      this.valorDiaria = null;
    }
    this.erro = '';
    this.mensagem = 'Classe excluída com sucesso.';
  }

  cancelar(formulario: NgForm): void {
    this.resetarFormulario(formulario);
    this.limparFeedback();
  }

  private atualizarLista(classes: ClasseRegistro[]): void {
    this.classes.set(classes);
    salvarColecao(CHAVE_STORAGE, classes);
  }

  private resetarFormulario(formulario: NgForm): void {
    this.editandoId = null;
    this.nome = '';
    this.valorDiaria = null;
    formulario.resetForm({ nome: '', valorDiaria: null });
  }

  private limparFeedback(): void {
    this.mensagem = '';
    this.erro = '';
  }

}

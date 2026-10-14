import { CurrencyPipe } from '@angular/common';
import { afterNextRender, Component, signal } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { finalize } from 'rxjs';
import {
  CadastroApiService,
  ClasseRegistro,
  mensagemDaApi,
} from '../../shared/cadastro-api.service';

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
  carregando = true;
  processando = false;

  constructor(private readonly api: CadastroApiService) {
    afterNextRender(() => this.recarregar());
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

    const idEditado = this.editandoId;
    const requisicao = idEditado === null
      ? this.api.cadastrarClasse(nome, valorDiaria)
      : this.api.atualizarClasse(idEditado, nome, valorDiaria);

    this.processando = true;
    requisicao.pipe(finalize(() => (this.processando = false))).subscribe({
      next: (classeSalva) => {
        this.classes.update((classes) =>
          idEditado === null
            ? [...classes, classeSalva]
            : classes.map((classe) => (classe.id === idEditado ? classeSalva : classe)),
        );
        this.mensagem = idEditado === null
          ? 'Classe cadastrada com sucesso.'
          : 'Classe atualizada com sucesso.';
        this.resetarFormulario(formulario);
      },
      error: (erro) => {
        this.erro = mensagemDaApi(erro, 'Não foi possível salvar a classe. Tente novamente.');
      },
    });
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

    this.limparFeedback();
    this.processando = true;
    this.api.excluirClasse(classe.id).pipe(finalize(() => (this.processando = false))).subscribe({
      next: () => {
        this.classes.update((classes) => classes.filter((item) => item.id !== classe.id));
        if (this.editandoId === classe.id) {
          this.editandoId = null;
          this.nome = '';
          this.valorDiaria = null;
        }
        this.mensagem = 'Classe excluída com sucesso.';
      },
      error: (erro) => {
        this.erro = mensagemDaApi(erro, 'Não foi possível excluir a classe. Tente novamente.');
      },
    });
  }

  cancelar(formulario: NgForm): void {
    this.resetarFormulario(formulario);
    this.limparFeedback();
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

  recarregar(): void {
    this.limparFeedback();
    this.carregando = true;
    this.api.listarClasses().pipe(finalize(() => (this.carregando = false))).subscribe({
      next: (classes) => this.classes.set(classes),
      error: (erro) => {
        this.erro = mensagemDaApi(
          erro,
          'Não foi possível carregar as classes. Confirme se o backend está em execução.',
        );
      },
    });
  }
}

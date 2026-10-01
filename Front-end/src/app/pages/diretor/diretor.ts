import { afterNextRender, Component, signal } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { carregarColecao, gerarProximoId, salvarColecao } from '../../shared/local-storage';

interface DiretorRegistro {
  id: number;
  nome: string;
}

const CHAVE_STORAGE = 'locadora:diretores';

@Component({
  selector: 'app-diretor',
  imports: [FormsModule],
  templateUrl: './diretor.html',
  styleUrl: './diretor.css',
})
export class Diretor {
  readonly diretores = signal<DiretorRegistro[]>([]);
  nome = '';
  busca = '';
  editandoId: number | null = null;
  mensagem = '';
  erro = '';

  constructor() {
    afterNextRender(() => this.diretores.set(carregarColecao<DiretorRegistro>(CHAVE_STORAGE)));
  }

  get diretoresFiltrados(): DiretorRegistro[] {
    const termo = this.busca.trim().toLocaleLowerCase('pt-BR');
    return termo
      ? this.diretores().filter((diretor) => diretor.nome.toLocaleLowerCase('pt-BR').includes(termo))
      : this.diretores();
  }

  salvar(formulario: NgForm): void {
    this.limparFeedback();
    const nome = this.nome.trim();

    if (!nome) {
      this.erro = 'Informe o nome do diretor.';
      return;
    }

    const nomeDuplicado = this.diretores().some(
      (diretor) =>
        diretor.id !== this.editandoId &&
        diretor.nome.localeCompare(nome, 'pt-BR', { sensitivity: 'accent' }) === 0,
    );

    if (nomeDuplicado) {
      this.erro = 'Já existe um diretor cadastrado com esse nome.';
      return;
    }

    if (this.editandoId !== null) {
      this.atualizarLista(
        this.diretores().map((diretor) =>
          diretor.id === this.editandoId ? { ...diretor, nome } : diretor,
        ),
      );
      this.mensagem = 'Diretor atualizado com sucesso.';
    } else {
      this.atualizarLista([...this.diretores(), { id: gerarProximoId(this.diretores()), nome }]);
      this.mensagem = 'Diretor cadastrado com sucesso.';
    }

    this.resetarFormulario(formulario);
  }

  editar(diretor: DiretorRegistro): void {
    this.limparFeedback();
    this.editandoId = diretor.id;
    this.nome = diretor.nome;
  }

  excluir(diretor: DiretorRegistro): void {
    if (!confirm(`Deseja excluir o diretor "${diretor.nome}"?`)) {
      return;
    }

    this.atualizarLista(this.diretores().filter((item) => item.id !== diretor.id));
    if (this.editandoId === diretor.id) {
      this.editandoId = null;
      this.nome = '';
    }
    this.erro = '';
    this.mensagem = 'Diretor excluído com sucesso.';
  }

  cancelar(formulario: NgForm): void {
    this.resetarFormulario(formulario);
    this.limparFeedback();
  }

  private atualizarLista(diretores: DiretorRegistro[]): void {
    this.diretores.set(diretores);
    salvarColecao(CHAVE_STORAGE, diretores);
  }

  private resetarFormulario(formulario: NgForm): void {
    this.editandoId = null;
    this.nome = '';
    formulario.resetForm({ nome: '' });
  }

  private limparFeedback(): void {
    this.mensagem = '';
    this.erro = '';
  }

}

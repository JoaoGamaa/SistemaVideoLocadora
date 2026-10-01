import { afterNextRender, Component, signal } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { carregarColecao, gerarProximoId, salvarColecao } from '../../shared/local-storage';

interface AtorRegistro {
  id: number;
  nome: string;
}

const CHAVE_STORAGE = 'locadora:atores';

@Component({
  selector: 'app-ator',
  imports: [FormsModule],
  templateUrl: './ator.html',
  styleUrl: './ator.css',
})
export class Ator {
  readonly atores = signal<AtorRegistro[]>([]);
  nome = '';
  busca = '';
  editandoId: number | null = null;
  mensagem = '';
  erro = '';

  constructor() {
    afterNextRender(() => this.atores.set(carregarColecao<AtorRegistro>(CHAVE_STORAGE)));
  }

  get atoresFiltrados(): AtorRegistro[] {
    const termo = this.busca.trim().toLocaleLowerCase('pt-BR');
    return termo
      ? this.atores().filter((ator) => ator.nome.toLocaleLowerCase('pt-BR').includes(termo))
      : this.atores();
  }

  salvar(formulario: NgForm): void {
    this.limparFeedback();
    const nome = this.nome.trim();

    if (!nome) {
      this.erro = 'Informe o nome do ator.';
      return;
    }

    const nomeDuplicado = this.atores().some(
      (ator) =>
        ator.id !== this.editandoId &&
        ator.nome.localeCompare(nome, 'pt-BR', { sensitivity: 'accent' }) === 0,
    );

    if (nomeDuplicado) {
      this.erro = 'Já existe um ator cadastrado com esse nome.';
      return;
    }

    if (this.editandoId !== null) {
      this.atualizarLista(
        this.atores().map((ator) => (ator.id === this.editandoId ? { ...ator, nome } : ator)),
      );
      this.mensagem = 'Ator atualizado com sucesso.';
    } else {
      this.atualizarLista([...this.atores(), { id: gerarProximoId(this.atores()), nome }]);
      this.mensagem = 'Ator cadastrado com sucesso.';
    }

    this.resetarFormulario(formulario);
  }

  editar(ator: AtorRegistro): void {
    this.limparFeedback();
    this.editandoId = ator.id;
    this.nome = ator.nome;
  }

  excluir(ator: AtorRegistro): void {
    if (!confirm(`Deseja excluir o ator "${ator.nome}"?`)) {
      return;
    }

    this.atualizarLista(this.atores().filter((item) => item.id !== ator.id));
    if (this.editandoId === ator.id) {
      this.editandoId = null;
      this.nome = '';
    }
    this.erro = '';
    this.mensagem = 'Ator excluído com sucesso.';
  }

  cancelar(formulario: NgForm): void {
    this.resetarFormulario(formulario);
    this.limparFeedback();
  }

  private atualizarLista(atores: AtorRegistro[]): void {
    this.atores.set(atores);
    salvarColecao(CHAVE_STORAGE, atores);
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

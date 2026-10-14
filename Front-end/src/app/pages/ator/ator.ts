import { afterNextRender, Component, signal } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { finalize } from 'rxjs';
import {
  AtorRegistro,
  CadastroApiService,
  mensagemDaApi,
} from '../../shared/cadastro-api.service';

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
  carregando = true;
  processando = false;

  constructor(private readonly api: CadastroApiService) {
    afterNextRender(() => this.recarregar());
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

    const idEditado = this.editandoId;
    const requisicao = idEditado === null
      ? this.api.cadastrarAtor(nome)
      : this.api.atualizarAtor(idEditado, nome);

    this.processando = true;
    requisicao.pipe(finalize(() => (this.processando = false))).subscribe({
      next: (atorSalvo) => {
        this.atores.update((atores) =>
          idEditado === null
            ? [...atores, atorSalvo]
            : atores.map((ator) => (ator.id === idEditado ? atorSalvo : ator)),
        );
        this.mensagem = idEditado === null
          ? 'Ator cadastrado com sucesso.'
          : 'Ator atualizado com sucesso.';
        this.resetarFormulario(formulario);
      },
      error: (erro) => {
        this.erro = mensagemDaApi(erro, 'Não foi possível salvar o ator. Tente novamente.');
      },
    });
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

    this.limparFeedback();
    this.processando = true;
    this.api.excluirAtor(ator.id).pipe(finalize(() => (this.processando = false))).subscribe({
      next: () => {
        this.atores.update((atores) => atores.filter((item) => item.id !== ator.id));
        if (this.editandoId === ator.id) {
          this.editandoId = null;
          this.nome = '';
        }
        this.mensagem = 'Ator excluído com sucesso.';
      },
      error: (erro) => {
        this.erro = mensagemDaApi(erro, 'Não foi possível excluir o ator. Tente novamente.');
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
    formulario.resetForm({ nome: '' });
  }

  private limparFeedback(): void {
    this.mensagem = '';
    this.erro = '';
  }

  recarregar(): void {
    this.limparFeedback();
    this.carregando = true;
    this.api.listarAtores().pipe(finalize(() => (this.carregando = false))).subscribe({
      next: (atores) => this.atores.set(atores),
      error: (erro) => {
        this.erro = mensagemDaApi(
          erro,
          'Não foi possível carregar os atores. Confirme se o backend está em execução.',
        );
      },
    });
  }
}

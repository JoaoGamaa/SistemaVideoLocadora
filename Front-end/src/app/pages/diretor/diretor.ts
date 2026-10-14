import { afterNextRender, Component, signal } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { finalize } from 'rxjs';
import {
  CadastroApiService,
  DiretorRegistro,
  mensagemDaApi,
} from '../../shared/cadastro-api.service';

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
  carregando = true;
  processando = false;

  constructor(private readonly api: CadastroApiService) {
    afterNextRender(() => this.recarregar());
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

    const idEditado = this.editandoId;
    const requisicao = idEditado === null
      ? this.api.cadastrarDiretor(nome)
      : this.api.atualizarDiretor(idEditado, nome);

    this.processando = true;
    requisicao.pipe(finalize(() => (this.processando = false))).subscribe({
      next: (diretorSalvo) => {
        this.diretores.update((diretores) =>
          idEditado === null
            ? [...diretores, diretorSalvo]
            : diretores.map((diretor) => (diretor.id === idEditado ? diretorSalvo : diretor)),
        );
        this.mensagem = idEditado === null
          ? 'Diretor cadastrado com sucesso.'
          : 'Diretor atualizado com sucesso.';
        this.resetarFormulario(formulario);
      },
      error: (erro) => {
        this.erro = mensagemDaApi(erro, 'Não foi possível salvar o diretor. Tente novamente.');
      },
    });
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

    this.limparFeedback();
    this.processando = true;
    this.api.excluirDiretor(diretor.id).pipe(finalize(() => (this.processando = false))).subscribe({
      next: () => {
        this.diretores.update((diretores) => diretores.filter((item) => item.id !== diretor.id));
        if (this.editandoId === diretor.id) {
          this.editandoId = null;
          this.nome = '';
        }
        this.mensagem = 'Diretor excluído com sucesso.';
      },
      error: (erro) => {
        this.erro = mensagemDaApi(erro, 'Não foi possível excluir o diretor. Tente novamente.');
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
    this.api.listarDiretores().pipe(finalize(() => (this.carregando = false))).subscribe({
      next: (diretores) => this.diretores.set(diretores),
      error: (erro) => {
        this.erro = mensagemDaApi(
          erro,
          'Não foi possível carregar os diretores. Confirme se o backend está em execução.',
        );
      },
    });
  }
}

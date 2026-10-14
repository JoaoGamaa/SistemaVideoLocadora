import { afterNextRender, Component, signal } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { finalize, forkJoin } from 'rxjs';
import {
  CadastroApiService,
  ClasseRegistro,
  DiretorRegistro,
  mensagemDaApi,
  TituloRegistro,
} from '../../shared/cadastro-api.service';

@Component({
  selector: 'app-titulo',
  imports: [FormsModule],
  templateUrl: './titulo.html',
  styleUrl: './titulo.css',
})
export class Titulo {
  readonly titulos = signal<TituloRegistro[]>([]);
  readonly diretores = signal<DiretorRegistro[]>([]);
  readonly classes = signal<ClasseRegistro[]>([]);
  readonly anoMaximo = new Date().getFullYear() + 1;

  nome = '';
  anoLancamento: number | null = null;
  genero = '';
  diretorId: number | null = null;
  classeId: number | null = null;
  busca = '';
  editandoId: number | null = null;
  mensagem = '';
  erro = '';
  carregando = true;
  processando = false;

  constructor(private readonly api: CadastroApiService) {
    afterNextRender(() => this.recarregar());
  }

  get titulosFiltrados(): TituloRegistro[] {
    const termo = this.busca.trim().toLocaleLowerCase('pt-BR');
    if (!termo) {
      return this.titulos();
    }

    return this.titulos().filter((titulo) =>
      [
        titulo.nome,
        titulo.anoLancamento.toString(),
        titulo.genero,
        titulo.diretor.nome,
        titulo.classe.nome,
      ].some((valor) => valor.toLocaleLowerCase('pt-BR').includes(termo)),
    );
  }

  salvar(formulario: NgForm): void {
    this.limparFeedback();
    const nome = this.nome.trim();
    const genero = this.genero.trim();
    const anoLancamento = Number(this.anoLancamento);

    if (!nome) {
      this.erro = 'Informe o nome do título.';
      return;
    }
    if (!Number.isInteger(anoLancamento) || anoLancamento < 1888 || anoLancamento > this.anoMaximo) {
      this.erro = `Informe um ano entre 1888 e ${this.anoMaximo}.`;
      return;
    }
    if (!genero) {
      this.erro = 'Informe o gênero do título.';
      return;
    }
    if (this.diretorId === null) {
      this.erro = 'Selecione o diretor.';
      return;
    }
    if (this.classeId === null) {
      this.erro = 'Selecione a classe.';
      return;
    }

    const duplicado = this.titulos().some(
      (titulo) =>
        titulo.id !== this.editandoId &&
        titulo.anoLancamento === anoLancamento &&
        titulo.nome.localeCompare(nome, 'pt-BR', { sensitivity: 'base' }) === 0,
    );
    if (duplicado) {
      this.erro = 'Já existe um título cadastrado com esse nome e ano.';
      return;
    }

    const idEditado = this.editandoId;
    const requisicao = idEditado === null
      ? this.api.cadastrarTitulo(
          nome,
          anoLancamento,
          genero,
          this.diretorId,
          this.classeId,
        )
      : this.api.atualizarTitulo(
          idEditado,
          nome,
          anoLancamento,
          genero,
          this.diretorId,
          this.classeId,
        );

    this.processando = true;
    requisicao.pipe(finalize(() => (this.processando = false))).subscribe({
      next: (tituloSalvo) => {
        this.titulos.update((titulos) =>
          idEditado === null
            ? [...titulos, tituloSalvo]
            : titulos.map((titulo) => (titulo.id === idEditado ? tituloSalvo : titulo)),
        );
        this.mensagem = idEditado === null
          ? 'Título cadastrado com sucesso.'
          : 'Título atualizado com sucesso.';
        this.resetarFormulario(formulario);
      },
      error: (erro) => {
        this.erro = mensagemDaApi(erro, 'Não foi possível salvar o título. Tente novamente.');
      },
    });
  }

  editar(titulo: TituloRegistro): void {
    this.limparFeedback();
    this.editandoId = titulo.id;
    this.nome = titulo.nome;
    this.anoLancamento = titulo.anoLancamento;
    this.genero = titulo.genero;
    this.diretorId = titulo.diretor.id;
    this.classeId = titulo.classe.id;
  }

  excluir(titulo: TituloRegistro): void {
    if (!confirm(`Deseja excluir o título "${titulo.nome}"?`)) {
      return;
    }

    this.limparFeedback();
    this.processando = true;
    this.api.excluirTitulo(titulo.id).pipe(finalize(() => (this.processando = false))).subscribe({
      next: () => {
        this.titulos.update((titulos) => titulos.filter((item) => item.id !== titulo.id));
        if (this.editandoId === titulo.id) {
          this.limparCampos();
        }
        this.mensagem = 'Título excluído com sucesso.';
      },
      error: (erro) => {
        this.erro = mensagemDaApi(erro, 'Não foi possível excluir o título. Tente novamente.');
      },
    });
  }

  cancelar(formulario: NgForm): void {
    this.resetarFormulario(formulario);
    this.limparFeedback();
  }

  recarregar(): void {
    this.limparFeedback();
    this.carregando = true;
    forkJoin({
      titulos: this.api.listarTitulos(),
      diretores: this.api.listarDiretores(),
      classes: this.api.listarClasses(),
    }).pipe(finalize(() => (this.carregando = false))).subscribe({
      next: ({ titulos, diretores, classes }) => {
        this.titulos.set(titulos);
        this.diretores.set(diretores);
        this.classes.set(classes);
      },
      error: (erro) => {
        this.erro = mensagemDaApi(
          erro,
          'Não foi possível carregar os títulos. Confirme se o backend está em execução.',
        );
      },
    });
  }

  private resetarFormulario(formulario: NgForm): void {
    this.limparCampos();
    formulario.resetForm({
      nome: '',
      anoLancamento: null,
      genero: '',
      diretorId: null,
      classeId: null,
    });
  }

  private limparCampos(): void {
    this.editandoId = null;
    this.nome = '';
    this.anoLancamento = null;
    this.genero = '';
    this.diretorId = null;
    this.classeId = null;
  }

  private limparFeedback(): void {
    this.mensagem = '';
    this.erro = '';
  }
}

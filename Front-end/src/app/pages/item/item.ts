import { DatePipe } from '@angular/common';
import { afterNextRender, Component, signal } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { finalize, forkJoin } from 'rxjs';
import {
  CadastroApiService,
  ItemRegistro,
  mensagemDaApi,
  TipoItem,
  TituloRegistro,
} from '../../shared/cadastro-api.service';

@Component({
  selector: 'app-item',
  imports: [FormsModule, DatePipe],
  templateUrl: './item.html',
  styleUrl: './item.css',
})
export class Item {
  readonly itens = signal<ItemRegistro[]>([]);
  readonly titulos = signal<TituloRegistro[]>([]);
  readonly dataMaxima = this.formatarDataLocal(new Date());

  numeroSerie = '';
  tipo: TipoItem | '' = '';
  dataAquisicao = '';
  disponivel = true;
  tituloId: number | null = null;
  busca = '';
  editandoId: number | null = null;
  mensagem = '';
  erro = '';
  carregando = true;
  processando = false;

  constructor(private readonly api: CadastroApiService) {
    afterNextRender(() => this.recarregar());
  }

  get itensFiltrados(): ItemRegistro[] {
    const termo = this.busca.trim().toLocaleLowerCase('pt-BR');
    if (!termo) {
      return this.itens();
    }

    return this.itens().filter((item) =>
      [
        item.numeroSerie,
        item.titulo.nome,
        this.rotuloTipo(item.tipo),
        item.disponivel ? 'disponível' : 'indisponível',
      ].some((valor) => valor.toLocaleLowerCase('pt-BR').includes(termo)),
    );
  }

  salvar(formulario: NgForm): void {
    this.limparFeedback();
    const numeroSerie = this.numeroSerie.trim();

    if (!numeroSerie) {
      this.erro = 'Informe o número de série do item.';
      return;
    }
    if (!this.tipo) {
      this.erro = 'Selecione o tipo de mídia.';
      return;
    }
    if (!this.dataAquisicao || this.dataAquisicao > this.dataMaxima) {
      this.erro = 'Informe uma data de aquisição válida.';
      return;
    }
    if (this.tituloId === null) {
      this.erro = 'Selecione o título do item.';
      return;
    }

    const duplicado = this.itens().some(
      (item) =>
        item.id !== this.editandoId &&
        item.numeroSerie.localeCompare(numeroSerie, 'pt-BR', { sensitivity: 'base' }) === 0,
    );
    if (duplicado) {
      this.erro = 'Já existe um item com esse número de série.';
      return;
    }

    const idEditado = this.editandoId;
    const requisicao = idEditado === null
      ? this.api.cadastrarItem(
          numeroSerie,
          this.tipo,
          this.dataAquisicao,
          this.disponivel,
          this.tituloId,
        )
      : this.api.atualizarItem(
          idEditado,
          numeroSerie,
          this.tipo,
          this.dataAquisicao,
          this.disponivel,
          this.tituloId,
        );

    this.processando = true;
    requisicao.pipe(finalize(() => (this.processando = false))).subscribe({
      next: (itemSalvo) => {
        this.itens.update((itens) =>
          idEditado === null
            ? [...itens, itemSalvo]
            : itens.map((item) => (item.id === idEditado ? itemSalvo : item)),
        );
        this.mensagem = idEditado === null
          ? 'Item cadastrado com sucesso.'
          : 'Item atualizado com sucesso.';
        this.resetarFormulario(formulario);
      },
      error: (erro) => {
        this.erro = mensagemDaApi(erro, 'Não foi possível salvar o item. Tente novamente.');
      },
    });
  }

  editar(item: ItemRegistro): void {
    this.limparFeedback();
    this.editandoId = item.id;
    this.numeroSerie = item.numeroSerie;
    this.tipo = item.tipo;
    this.dataAquisicao = item.dataAquisicao;
    this.disponivel = item.disponivel;
    this.tituloId = item.titulo.id;
  }

  excluir(item: ItemRegistro): void {
    if (!confirm(`Deseja excluir o item "${item.numeroSerie}"?`)) {
      return;
    }

    this.limparFeedback();
    this.processando = true;
    this.api.excluirItem(item.id).pipe(finalize(() => (this.processando = false))).subscribe({
      next: () => {
        this.itens.update((itens) => itens.filter((registro) => registro.id !== item.id));
        if (this.editandoId === item.id) {
          this.limparCampos();
        }
        this.mensagem = 'Item excluído com sucesso.';
      },
      error: (erro) => {
        this.erro = mensagemDaApi(erro, 'Não foi possível excluir o item. Tente novamente.');
      },
    });
  }

  cancelar(formulario: NgForm): void {
    this.resetarFormulario(formulario);
    this.limparFeedback();
  }

  rotuloTipo(tipo: TipoItem): string {
    const rotulos: Record<TipoItem, string> = {
      DVD: 'DVD',
      BLU_RAY: 'Blu-ray',
      VHS: 'VHS',
    };
    return rotulos[tipo];
  }

  recarregar(): void {
    this.limparFeedback();
    this.carregando = true;
    forkJoin({
      itens: this.api.listarItens(),
      titulos: this.api.listarTitulos(),
    }).pipe(finalize(() => (this.carregando = false))).subscribe({
      next: ({ itens, titulos }) => {
        this.itens.set(itens);
        this.titulos.set(titulos);
      },
      error: (erro) => {
        this.erro = mensagemDaApi(
          erro,
          'Não foi possível carregar os itens. Confirme se o backend está em execução.',
        );
      },
    });
  }

  private resetarFormulario(formulario: NgForm): void {
    this.limparCampos();
    formulario.resetForm({
      numeroSerie: '',
      tipo: '',
      dataAquisicao: '',
      disponivel: true,
      tituloId: null,
    });
  }

  private limparCampos(): void {
    this.editandoId = null;
    this.numeroSerie = '';
    this.tipo = '';
    this.dataAquisicao = '';
    this.disponivel = true;
    this.tituloId = null;
  }

  private limparFeedback(): void {
    this.mensagem = '';
    this.erro = '';
  }

  private formatarDataLocal(data: Date): string {
    const ano = data.getFullYear();
    const mes = String(data.getMonth() + 1).padStart(2, '0');
    const dia = String(data.getDate()).padStart(2, '0');
    return `${ano}-${mes}-${dia}`;
  }
}

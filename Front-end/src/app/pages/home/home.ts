import { afterNextRender, Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { CadastroApiService } from '../../shared/cadastro-api.service';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  readonly totalAtores = signal(0);
  readonly totalClasses = signal(0);
  readonly totalDiretores = signal(0);
  readonly totalTitulos = signal(0);
  readonly totalItens = signal(0);
  readonly dadosIndisponiveis = signal(false);

  constructor(private readonly api: CadastroApiService) {
    afterNextRender(() => {
      forkJoin({
        atores: this.api.listarAtores(),
        classes: this.api.listarClasses(),
        diretores: this.api.listarDiretores(),
        titulos: this.api.listarTitulos(),
        itens: this.api.listarItens(),
      }).subscribe({
        next: ({ atores, classes, diretores, titulos, itens }) => {
          this.totalAtores.set(atores.length);
          this.totalClasses.set(classes.length);
          this.totalDiretores.set(diretores.length);
          this.totalTitulos.set(titulos.length);
          this.totalItens.set(itens.length);
          this.dadosIndisponiveis.set(false);
        },
        error: () => this.dadosIndisponiveis.set(true),
      });
    });
  }
}

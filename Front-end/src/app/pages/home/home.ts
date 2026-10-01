import { afterNextRender, Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { carregarColecao } from '../../shared/local-storage';

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

  constructor() {
    afterNextRender(() => {
      this.totalAtores.set(carregarColecao('locadora:atores').length);
      this.totalClasses.set(carregarColecao('locadora:classes').length);
      this.totalDiretores.set(carregarColecao('locadora:diretores').length);
    });
  }
}

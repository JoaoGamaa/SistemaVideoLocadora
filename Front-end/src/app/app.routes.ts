import { Routes } from '@angular/router';
import { Ator } from './pages/ator/ator';
import { Classe } from './pages/classe/classe';
import { Diretor } from './pages/diretor/diretor';
import { Home } from './pages/home/home';

export const routes: Routes = [
  { path: '', component: Home, title: 'Início | Locadora' },
  { path: 'atores', component: Ator, title: 'Atores | Locadora' },
  { path: 'classes', component: Classe, title: 'Classes | Locadora' },
  { path: 'diretores', component: Diretor, title: 'Diretores | Locadora' },
  { path: '**', redirectTo: '' },
];

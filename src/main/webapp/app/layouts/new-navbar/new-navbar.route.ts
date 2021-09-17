import { Route } from '@angular/router';

import { NewNavbarComponent } from './new-navbar.component';

export const navbarRoute: Route = {
  path: '',
  component: NewNavbarComponent,
  outlet: 'new-navbar',
};

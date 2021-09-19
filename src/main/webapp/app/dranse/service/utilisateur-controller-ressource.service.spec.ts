import { TestBed } from '@angular/core/testing';

import { UtilisateurControllerRessourceService } from './utilisateur-controller-ressource.service';

describe('UtilisateurControllerRessourceService', () => {
  let service: UtilisateurControllerRessourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UtilisateurControllerRessourceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

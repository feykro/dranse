import { TestBed } from '@angular/core/testing';

import { CommandeControllerRessourceService } from './commande-controller-ressource.service';

describe('CommandeControllerRessourceService', () => {
  let service: CommandeControllerRessourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommandeControllerRessourceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

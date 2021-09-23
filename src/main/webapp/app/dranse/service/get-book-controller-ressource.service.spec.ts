import { TestBed } from '@angular/core/testing';

import { GetBookControllerRessourceService } from './get-book-controller-ressource.service';

describe('GetBookControllerRessourceService', () => {
  let service: GetBookControllerRessourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GetBookControllerRessourceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

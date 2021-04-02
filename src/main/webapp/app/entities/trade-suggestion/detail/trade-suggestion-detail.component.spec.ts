import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TradeSuggestionDetailComponent } from './trade-suggestion-detail.component';

describe('Component Tests', () => {
  describe('TradeSuggestion Management Detail Component', () => {
    let comp: TradeSuggestionDetailComponent;
    let fixture: ComponentFixture<TradeSuggestionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TradeSuggestionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tradeSuggestion: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TradeSuggestionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TradeSuggestionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tradeSuggestion on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tradeSuggestion).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

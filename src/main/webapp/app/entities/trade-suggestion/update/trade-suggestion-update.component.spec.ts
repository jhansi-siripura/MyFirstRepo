jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TradeSuggestionService } from '../service/trade-suggestion.service';
import { ITradeSuggestion, TradeSuggestion } from '../trade-suggestion.model';

import { TradeSuggestionUpdateComponent } from './trade-suggestion-update.component';

describe('Component Tests', () => {
  describe('TradeSuggestion Management Update Component', () => {
    let comp: TradeSuggestionUpdateComponent;
    let fixture: ComponentFixture<TradeSuggestionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tradeSuggestionService: TradeSuggestionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TradeSuggestionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TradeSuggestionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TradeSuggestionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tradeSuggestionService = TestBed.inject(TradeSuggestionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const tradeSuggestion: ITradeSuggestion = { id: 456 };

        activatedRoute.data = of({ tradeSuggestion });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tradeSuggestion));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tradeSuggestion = { id: 123 };
        spyOn(tradeSuggestionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tradeSuggestion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tradeSuggestion }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tradeSuggestionService.update).toHaveBeenCalledWith(tradeSuggestion);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tradeSuggestion = new TradeSuggestion();
        spyOn(tradeSuggestionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tradeSuggestion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tradeSuggestion }));
        saveSubject.complete();

        // THEN
        expect(tradeSuggestionService.create).toHaveBeenCalledWith(tradeSuggestion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tradeSuggestion = { id: 123 };
        spyOn(tradeSuggestionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tradeSuggestion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tradeSuggestionService.update).toHaveBeenCalledWith(tradeSuggestion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

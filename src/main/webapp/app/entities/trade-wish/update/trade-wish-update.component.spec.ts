jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TradeWishService } from '../service/trade-wish.service';
import { ITradeWish, TradeWish } from '../trade-wish.model';

import { TradeWishUpdateComponent } from './trade-wish-update.component';

describe('Component Tests', () => {
  describe('TradeWish Management Update Component', () => {
    let comp: TradeWishUpdateComponent;
    let fixture: ComponentFixture<TradeWishUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tradeWishService: TradeWishService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TradeWishUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TradeWishUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TradeWishUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tradeWishService = TestBed.inject(TradeWishService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const tradeWish: ITradeWish = { id: 456 };

        activatedRoute.data = of({ tradeWish });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tradeWish));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tradeWish = { id: 123 };
        spyOn(tradeWishService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tradeWish });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tradeWish }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tradeWishService.update).toHaveBeenCalledWith(tradeWish);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tradeWish = new TradeWish();
        spyOn(tradeWishService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tradeWish });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tradeWish }));
        saveSubject.complete();

        // THEN
        expect(tradeWishService.create).toHaveBeenCalledWith(tradeWish);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tradeWish = { id: 123 };
        spyOn(tradeWishService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tradeWish });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tradeWishService.update).toHaveBeenCalledWith(tradeWish);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

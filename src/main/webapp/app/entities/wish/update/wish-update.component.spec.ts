jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WishService } from '../service/wish.service';
import { IWish, Wish } from '../wish.model';

import { WishUpdateComponent } from './wish-update.component';

describe('Component Tests', () => {
  describe('Wish Management Update Component', () => {
    let comp: WishUpdateComponent;
    let fixture: ComponentFixture<WishUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let wishService: WishService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WishUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WishUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WishUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      wishService = TestBed.inject(WishService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const wish: IWish = { id: 456 };

        activatedRoute.data = of({ wish });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(wish));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const wish = { id: 123 };
        spyOn(wishService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ wish });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: wish }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(wishService.update).toHaveBeenCalledWith(wish);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const wish = new Wish();
        spyOn(wishService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ wish });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: wish }));
        saveSubject.complete();

        // THEN
        expect(wishService.create).toHaveBeenCalledWith(wish);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const wish = { id: 123 };
        spyOn(wishService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ wish });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(wishService.update).toHaveBeenCalledWith(wish);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

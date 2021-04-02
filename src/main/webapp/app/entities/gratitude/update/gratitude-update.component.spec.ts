jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GratitudeService } from '../service/gratitude.service';
import { IGratitude, Gratitude } from '../gratitude.model';

import { GratitudeUpdateComponent } from './gratitude-update.component';

describe('Component Tests', () => {
  describe('Gratitude Management Update Component', () => {
    let comp: GratitudeUpdateComponent;
    let fixture: ComponentFixture<GratitudeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let gratitudeService: GratitudeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GratitudeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(GratitudeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GratitudeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      gratitudeService = TestBed.inject(GratitudeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const gratitude: IGratitude = { id: 456 };

        activatedRoute.data = of({ gratitude });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(gratitude));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const gratitude = { id: 123 };
        spyOn(gratitudeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ gratitude });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: gratitude }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(gratitudeService.update).toHaveBeenCalledWith(gratitude);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const gratitude = new Gratitude();
        spyOn(gratitudeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ gratitude });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: gratitude }));
        saveSubject.complete();

        // THEN
        expect(gratitudeService.create).toHaveBeenCalledWith(gratitude);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const gratitude = { id: 123 };
        spyOn(gratitudeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ gratitude });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(gratitudeService.update).toHaveBeenCalledWith(gratitude);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWish } from '../wish.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { WishService } from '../service/wish.service';
import { WishDeleteDialogComponent } from '../delete/wish-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-wish',
  templateUrl: './wish.component.html',
})
export class WishComponent implements OnInit {
  wishes: IWish[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected wishService: WishService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.wishes = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.wishService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IWish[]>) => {
          this.isLoading = false;
          this.paginateWishes(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.wishes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IWish): number {
    return item.id!;
  }

  delete(wish: IWish): void {
    const modalRef = this.modalService.open(WishDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.wish = wish;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateWishes(data: IWish[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.wishes.push(d);
      }
    }
  }
}

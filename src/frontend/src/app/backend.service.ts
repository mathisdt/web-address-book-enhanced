import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map, Observable} from 'rxjs';
import {Family} from './model/Family';
import {Person} from './model/Person';
import {environment} from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BackendService {
  private headers = {"Accept": "application/json", "Content-Type": "application/json"};
  baseUrl: string = environment.backendUrl;
  private http: HttpClient = inject(HttpClient);

  private repairFamilyTypes(ff: Family[]): Family[] {
    if (ff) {
      return ff.map(f => this.repairFamilyType(f));
    } else {
      return ff;
    }
  }

  private repairFamilyType(f: Family): Family {
    if (f.members) {
      f.members = f.members.map(p => this.repairPersonType(p));
    }
    return Object.assign(Object.create(Family.prototype), f);
  }

  private repairPersonType(p: Person): Person {
    return Object.assign(Object.create(Person.prototype), p)
  }

  readAllFamilies(): Observable<Family[]> {
    return this.http.get<Family[]>(`${this.baseUrl}/family`, {headers: this.headers})
      .pipe(map(ff => this.repairFamilyTypes(ff)));
  }

  createFamily(): Observable<Family> {
    return this.http.post<Family>(`${this.baseUrl}/family`, {headers: this.headers})
      .pipe(map(f => this.repairFamilyType(f)));
  }

  readFamily(id: number): Observable<Family> {
    return this.http.get<Family>(`${this.baseUrl}/family/${id}`, {headers: this.headers})
      .pipe(map(f => this.repairFamilyType(f)));
  }

  updateFamily(family: Family): Observable<Family> {
    return this.http.put<Family>(`${this.baseUrl}/family/${family.id}`, family, {headers: this.headers})
      .pipe(map(f => this.repairFamilyType(f)));
  }

  deleteFamily(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/family/${id}`, {headers: this.headers});
  }

  createPersonForFamily(familyId: number): Observable<Person> {
    return this.http.post<Person>(`${this.baseUrl}/family/${familyId}`, {}, {headers: this.headers})
      .pipe(map(p => this.repairPersonType(p)));
  }

  readPerson(id: number): Observable<Person> {
    return this.http.get<Person>(`${this.baseUrl}/person/${id}`, {headers: this.headers})
      .pipe(map(p => this.repairPersonType(p)));
  }

  updatePerson(person: Person): Observable<Person> {
    return this.http.put<Person>(`${this.baseUrl}/person/${person.id}`, person, {headers: this.headers})
      .pipe(map(p => this.repairPersonType(p)));
  }

  deletePerson(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/person/${id}`, {headers: this.headers});
  }

  movePersonDown(personId: number): Observable<Family> {
    return this.http.put<Family>(`${this.baseUrl}/person/${personId}/moveDown`, {headers: this.headers})
      .pipe(map(f => this.repairFamilyType(f)));
  }

  movePersonUp(personId: number): Observable<Family> {
    return this.http.put<Family>(`${this.baseUrl}/person/${personId}/moveUp`, {headers: this.headers})
      .pipe(map(f => this.repairFamilyType(f)));
  }
}

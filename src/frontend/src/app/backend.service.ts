import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
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

  readAllFamilies(): Observable<Family[]> {
    return this.http.get<Family[]>(`${this.baseUrl}/family`, {headers: this.headers});
  }

  createFamily(): Observable<Family> {
    return this.http.post<Family>(`${this.baseUrl}/family`, {headers: this.headers});
  }

  readFamily(id: number): Observable<Family> {
    return this.http.get<Family>(`${this.baseUrl}/family/${id}`, {headers: this.headers});
  }

  updateFamily(family: Family): Observable<Family> {
    return this.http.put<Family>(`${this.baseUrl}/family/${family.id}`, family, {headers: this.headers});
  }

  deleteFamily(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/family/${id}`, {headers: this.headers});
  }

  createPersonForFamily(familyId: number): Observable<Person> {
    return this.http.post<Person>(`${this.baseUrl}/family/${familyId}`, {}, {headers: this.headers});
  }

  readPerson(id: number): Observable<Person> {
    return this.http.get<Person>(`${this.baseUrl}/person/${id}`, {headers: this.headers});
  }

  updatePerson(person: Person): Observable<Person> {
    return this.http.put<Person>(`${this.baseUrl}/person/${person.id}`, person, {headers: this.headers});
  }

  deletePerson(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/person/${id}`, {headers: this.headers});
  }

  movePersonDown(personId: number): Observable<Family> {
    return this.http.put<Family>(`${this.baseUrl}/person/${personId}/moveDown`, {headers: this.headers});
  }

  movePersonUp(personId: number): Observable<Family> {
    return this.http.put<Family>(`${this.baseUrl}/person/${personId}/moveUp`, {headers: this.headers});
  }
}

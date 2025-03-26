import {Component, inject, input, output, signal} from '@angular/core';
import {Person} from '../model/Person';
import {Family} from '../model/Family';
import {FormsModule} from '@angular/forms';
import {BackendService} from '../backend.service';

@Component({
  selector: 'person-component',
  imports: [
    FormsModule
  ],
  templateUrl: './person.component.html',
  styleUrl: './person.component.css'
})
export class PersonComponent {
  private backend: BackendService = inject(BackendService);
  person = input.required<Person>();
  personChanged = output<Person>();
  personDeleted = output<Person>();
  personReordered = output<Family>();
  protected isEdit = signal(false);
  personForEdit: Person = new Person(null, null, null, null, null, null, null);

  edit() {
    this.personForEdit = new Person(
      this.person().firstName,
      this.person().lastName,
      this.person().birthday,
      this.person().contact1,
      this.person().contact2,
      this.person().contact3,
      this.person().remarks);

    this.isEdit.set(true);
  }

  delete() {
    this.backend.deletePerson(<number>this.person().id).subscribe(
      () => {
        console.log(`deleted person with ID ${this.person().id}`);
        this.personDeleted.emit(this.person());
      }
    );
  }

  save() {
    this.person().takeValuesFrom(this.personForEdit);

    this.backend.updatePerson(this.person()).subscribe(
      () => {
        this.personChanged.emit(this.person());
        console.log(`saved person ${this.person().id}`);
      }
    );

    this.isEdit.set(false);
  }

  cancel() {
    this.isEdit.set(false);
  }

  moveUp() {
    this.backend.movePersonUp(<number>this.person().id).subscribe(
      family => this.personReordered.emit(family)
    );
  }

  moveDown() {
    this.backend.movePersonDown(<number>this.person().id).subscribe(
      family => this.personReordered.emit(family)
    );
  }
}

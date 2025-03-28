import {Component, inject, input, output, signal} from '@angular/core';
import {Person} from './model/Person';
import {Family} from '../family/model/Family';
import {FormsModule} from '@angular/forms';
import {BackendService} from '../backend.service';
import {PersonHelper} from './model/PersonHelper';

@Component({
  selector: 'person-component',
  imports: [
    FormsModule
  ],
  templateUrl: './person.component.html'
})
export class PersonComponent {
  private readonly backend = inject(BackendService);
  person = input.required<Person>();
  personChanged = output<Person>();
  personDeleted = output<Person>();
  personReordered = output<Family>();
  protected isEdit = signal(false);
  personForEdit: Person = <Person>{};

  fields = [
    {key: 'firstName', title: 'Firstname'},
    {key: 'lastName', title: 'Lastname'},
    {key: 'birthday', title: 'Birthday'},
    {key: 'contact1', title: 'Contact 1'},
    {key: 'contact2', title: 'Contact 2'},
    {key: 'contact3', title: 'Contact 3'},
    {key: 'remarks', title: 'Remarks'}
  ];

  edit() {
    this.personForEdit = PersonHelper.createPerson(
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
    this.backend.deletePerson(<number>this.person().id).subscribe({
      next: () => {
        console.log(`deleted person with ID ${this.person().id}`);
        this.personDeleted.emit(this.person());
      },
      error: error => {
        console.error(`failed to delete person with ID ${this.person().id}:`, error);
      }
    });
  }

  save() {
    PersonHelper.copyPersonValues(this.personForEdit, this.person());

    this.backend.updatePerson(this.person()).subscribe({
      next: () => {
        this.personChanged.emit(this.person());
        console.log(`saved person with ID ${this.person().id}`);
      },
      error: error => {
        console.error(`failed to save person with ID ${this.person().id}:`, error);
      }
    });

    this.isEdit.set(false);
  }

  cancel() {
    this.isEdit.set(false);
  }

  moveUp() {
    this.backend.movePersonUp(<number>this.person().id).subscribe({
      next: family => {
        console.log(`moved person with ID ${this.person().id} up`);
        this.personReordered.emit(family);
      },
      error: error => console.error(`failed to move person with ID ${this.person().id} up:`, error)
    });
  }

  moveDown() {
    this.backend.movePersonDown(<number>this.person().id).subscribe({
      next: family => {
        console.log(`moved person with ID ${this.person().id} down`);
        this.personReordered.emit(family);
      },
      error: error => console.error(`failed to move person with ID ${this.person().id} down:`, error)
    });
  }
}

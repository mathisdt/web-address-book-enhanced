import {Component, inject, input, Input, OnInit, output, signal} from '@angular/core';
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
export class PersonComponent implements OnInit {
  private backend: BackendService = inject(BackendService);
  person = input.required<Person>();
  personDeleted = output<Person>();
  protected isEdit = signal(false);
  personForEdit: Person = {
    birthday: null,
    contact1: null,
    contact2: null,
    contact3: null,
    firstName: null,
    id: null,
    lastName: null,
    lastUpdate: null,
    ordering: null,
    remarks: null
  };

  ngOnInit(): void {
    this.personForEdit = this.person();
  }

  edit() {
    this.personForEdit = {
      firstName: this.person().firstName,
      lastName: this.person().lastName,
      birthday: this.person().birthday,
      contact1: this.person().contact1,
      contact2: this.person().contact2,
      contact3: this.person().contact3,
      remarks: this.person().remarks,
      id: null,
      lastUpdate: null,
      ordering: null

    };

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
    this.person().firstName = this.personForEdit.firstName;
    this.person().lastName = this.personForEdit.lastName;
    this.person().birthday = this.personForEdit.birthday;
    this.person().contact1 = this.personForEdit.contact1 === null ? null : this.personForEdit.contact1.trim();
    this.person().contact2 = this.personForEdit.contact2 === null ? null : this.personForEdit.contact2.trim();
    this.person().contact3 = this.personForEdit.contact3 === null ? null : this.personForEdit.contact3.trim();
    this.person().remarks = this.personForEdit.remarks;

    this.backend.updatePerson(this.person()).subscribe(
      () => console.log(`saved person ${this.person().id}`)
    );

    this.isEdit.set(false);
  }

  cancel() {
    this.isEdit.set(false);
  }
}

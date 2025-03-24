import {Component, inject, input, OnInit, output, signal} from '@angular/core';
import {Family} from '../model/Family';
import {Person} from '../model/Person';
import {PersonComponent} from '../person-component/person.component';
import {FormsModule} from '@angular/forms';
import {BackendService} from '../backend.service';

@Component({
  selector: 'family-component',
  imports: [
    PersonComponent,
    FormsModule
  ],
  templateUrl: './family.component.html',
  styleUrl: './family.component.css'
})
export class FamilyComponent implements OnInit {
  private backend: BackendService = inject(BackendService);
  family = input.required<Family>();
  familyDeleted = output<Family>();
  protected isEdit = signal(false);
  familyForEdit : Family = {
    city: null,
    contact1: null,
    contact2: null,
    contact3: null,
    id: null,
    lastName: null,
    lastUpdate: null,
    postalCode: null,
    remarks: null,
    street: null,
    members: []
  };

  ngOnInit(): void {
    this.familyForEdit = this.family();
  }

  edit() {
    this.familyForEdit = {
      lastName: this.family().lastName,
      street: this.family().street,
      postalCode: this.family().postalCode,
      city: this.family().city,
      contact1: this.family().contact1,
      contact2: this.family().contact2,
      contact3: this.family().contact3,
      remarks: this.family().remarks,
      id: null,
      lastUpdate: null,
      members: null
    };

    this.isEdit.set(true);
  }

  delete() {
    this.backend.deleteFamily(<number>this.family().id).subscribe(
      () => {
        console.log(`deleted family ${this.family().id}`);
        this.familyDeleted.emit(this.family());
      }
    );
  }

  addMember() {
    this.backend.createPersonForFamily(<number>this.family().id).subscribe(
      created => {
        if (this.family().members === null) {
          this.family().members = new Array<Person>();
        }
        // @ts-ignore
        this.family().members.push(created);
        console.log(`created person ${created.id} for family ${this.family().id}`)
      }
    )
  }

  memberDeleted(deletedMember: Person) {
    this.family().members =
      this.family().members?.filter((p: Person) => p.id !== deletedMember.id);
  }

  save() {
    this.family().lastName = this.familyForEdit.lastName;
    this.family().street = this.familyForEdit.street;
    this.family().postalCode = this.familyForEdit.postalCode;
    this.family().city = this.familyForEdit.city;
    this.family().contact1 = this.familyForEdit.contact1 === null ? null : this.familyForEdit.contact1.trim();
    this.family().contact2 = this.familyForEdit.contact2 === null ? null : this.familyForEdit.contact2.trim();
    this.family().contact3 = this.familyForEdit.contact3 === null ? null : this.familyForEdit.contact3.trim();
    this.family().remarks = this.familyForEdit.remarks;

    this.backend.updateFamily(this.family()).subscribe(
      () => console.log(`saved family with ID ${this.family().id}`)
    );

    this.isEdit.set(false);
  }

  cancel() {
    this.isEdit.set(false);
  }
}

import {Component, inject, input, output, signal} from '@angular/core';
import {Family} from './model/Family';
import {FamilyHelper} from './model/FamilyHelper';
import {Person} from '../person/model/Person';
import {PersonComponent} from '../person/person.component';
import {FormsModule} from '@angular/forms';
import {BackendService} from '../backend.service';
import {PersonHelper} from '../person/model/PersonHelper';

@Component({
  selector: 'family-component',
  imports: [
    PersonComponent,
    FormsModule
  ],
  templateUrl: './family.component.html',
  styleUrl: './family.component.css'
})
export class FamilyComponent {
  private readonly backend = inject(BackendService);
  family = input.required<Family>();
  familyChanged = output<Family>();
  familyDeleted = output<Family>();
  protected isEdit = signal(false);
  familyForEdit: Family = <Family>{}; // im Model null initialisieren

  fields = ['lastName', 'street', 'postalCode', 'city', 'contact1', 'contact2', 'contact3', 'remarks'];

  edit() {
    // object destructuring:
    const {lastName, street, postalCode, city, contact1, contact2, contact3, remarks} = this.family();
    this.familyForEdit = FamilyHelper.createFamily(lastName, street, postalCode, city, contact1, contact2, contact3, remarks);

    this.isEdit.set(true);
  }

  delete() {
    this.backend.deleteFamily(<number>this.family().id).subscribe({
      next: () => {
        console.log(`deleted family ${this.family().id}`);
        this.familyDeleted.emit(this.family());
      },
      error: error => {
        console.error(`failed to delete family ${this.family().id}:`, error);
      }
    });
  }

  addMember() {
    this.backend.createPersonForFamily(<number>this.family().id).subscribe({
      next: created => {
        // initialize with empty array if null:
        this.family().members ??= [];

        this.family().members?.push(created);
        this.familyChanged.emit(this.family());
        console.log(`created person ${created.id} for family ${this.family().id}`)
      },
      error: error => {
        console.error(`failed to create person for family ${this.family().id}:`, error);
      }
    });
  }

  memberDeleted(deletedMember: Person) {
    this.family().members =
      this.family().members?.filter((p: Person) => p.id !== deletedMember.id);
    this.familyChanged.emit(this.family());
  }

  membersReordered(familyWithReorderedMembers: Family) {
    this.family().members = familyWithReorderedMembers.members;
    this.familyChanged.emit(this.family());
  }

  save() {
    FamilyHelper.copyFamilyValues(this.familyForEdit, this.family());

    this.backend.updateFamily(this.family()).subscribe({
      next: () => {
        this.familyChanged.emit(this.family());
        console.log(`saved family with ID ${this.family().id}`);
      },
      error: error => {
        console.error(`failed to save family with ID ${this.family().id}:`, error);
      }
    });

    this.isEdit.set(false);
  }

  cancel() {
    this.isEdit.set(false);
  }

  personChanged(changedPerson: Person) {
    for (const person of this.family().members!) {
      if (person.id === changedPerson.id) {
        PersonHelper.copyPersonValues(changedPerson, person);
        break;
      }
    }
    this.familyChanged.emit(this.family());
  }
}

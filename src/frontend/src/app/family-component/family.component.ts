import {Component, inject, input, output, signal} from '@angular/core';
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
export class FamilyComponent {
  private backend: BackendService = inject(BackendService);
  family = input.required<Family>();
  familyChanged = output<Family>();
  familyDeleted = output<Family>();
  protected isEdit = signal(false);
  familyForEdit : Family = new Family(null, null, null, null, null, null, null, null);

  edit() {
    this.familyForEdit = new Family(
      this.family().lastName,
      this.family().street,
      this.family().postalCode,
      this.family().city,
      this.family().contact1,
      this.family().contact2,
      this.family().contact3,
      this.family().remarks);

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
        this.familyChanged.emit(this.family());
        console.log(`created person ${created.id} for family ${this.family().id}`)
      }
    )
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
    this.family().takeValuesFrom(this.familyForEdit);

    this.backend.updateFamily(this.family()).subscribe(
      () => {
        this.familyChanged.emit(this.family());
        console.log(`saved family with ID ${this.family().id}`);
      }
    );

    this.isEdit.set(false);
  }

  cancel() {
    this.isEdit.set(false);
  }

  personChanged(changedPerson: Person) {
    for (const person of this.family().members!) {
      if (person.id === changedPerson.id) {
        person.takeValuesFrom(changedPerson);
        break;
      }
    }
    this.familyChanged.emit(this.family());
  }
}

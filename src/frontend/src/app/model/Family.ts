import {Person} from './Person';

export class Family {
  id: number | null;
  lastUpdate: string | null;
  lastName: string | null;
  street: string | null;
  postalCode: string | null;
  city: string | null;
  contact1: string | null;
  contact2: string | null;
  contact3: string | null;
  remarks: string | null;
  members: Person[] | null | undefined;

  constructor(lastName: string | null, street: string | null, postalCode: string | null, city: string | null,
              contact1: string | null, contact2: string | null, contact3: string | null, remarks: string | null) {
    this.id = null;
    this.lastUpdate = null;
    this.members = Array<Person>();
    this.lastName = lastName;
    this.street = street;
    this.postalCode = postalCode;
    this.city = city;
    this.contact1 = contact1;
    this.contact2 = contact2;
    this.contact3 = contact3;
    this.remarks = remarks;
  }

  public takeValuesFrom(f: Family): void {
    this.lastName = f.lastName;
    this.street = f.street;
    this.postalCode = f.postalCode;
    this.city = f.city;
    this.contact1 = f.contact1 === null ? null : f.contact1.trim();
    this.contact2 = f.contact2 === null ? null : f.contact2.trim();
    this.contact3 = f.contact3 === null ? null : f.contact3.trim();
    this.remarks = f.remarks;
  }

  public takeMembersFrom(f: Family): void {
    this.members = f.members;
  }
}

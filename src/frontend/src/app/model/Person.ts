export class Person {
  id: number | null;
  lastUpdate: string | null;
  firstName: string | null;
  lastName: string | null;
  birthday: string | null;
  contact1: string | null;
  contact2: string | null;
  contact3: string | null;
  remarks: string | null;
  ordering: number | null;


  constructor(firstName: string | null, lastName: string | null, birthday: string | null,
              contact1: string | null, contact2: string | null, contact3: string | null, remarks: string | null) {
    this.id = null;
    this.lastUpdate = null;
    this.ordering = null;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthday = birthday;
    this.contact1 = contact1;
    this.contact2 = contact2;
    this.contact3 = contact3;
    this.remarks = remarks;
  }

  public takeValuesFrom(p: Person): void {
    this.firstName = p.firstName;
    this.lastName = p.lastName;
    this.birthday = p.birthday;
    this.contact1 = p.contact1 === null ? null : p.contact1.trim();
    this.contact2 = p.contact2 === null ? null : p.contact2.trim();
    this.contact3 = p.contact3 === null ? null : p.contact3.trim();
    this.remarks = p.remarks;
  }
}

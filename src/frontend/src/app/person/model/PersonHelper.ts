import {Person} from './Person';

export class PersonHelper {
  public static createPerson(firstName: string | null, lastName: string | null, birthday: string | null,
                            contact1: string | null, contact2: string | null, contact3: string | null, remarks: string | null) {
    const p: Person = <Person>{};
    p.id = null;
    p.lastUpdate = null;
    p.ordering = null;
    p.firstName = firstName;
    p.lastName = lastName;
    p.birthday = birthday;
    p.contact1 = contact1;
    p.contact2 = contact2;
    p.contact3 = contact3;
    p.remarks = remarks;
    return p;
  }

  public static copyPersonValues(source: Person, target: Person): void {
    target.firstName = source.firstName;
    target.lastName = source.lastName;
    target.birthday = source.birthday;
    target.contact1 = source.contact1 === null ? null : source.contact1.trim();
    target.contact2 = source.contact2 === null ? null : source.contact2.trim();
    target.contact3 = source.contact3 === null ? null : source.contact3.trim();
    target.remarks = source.remarks;
  }
}

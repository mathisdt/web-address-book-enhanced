import {Family} from './Family';

export class FamilyHelper {

  public static createFamily(
    lastName: string | null = null,
    street: string | null = null,
    postalCode: string | null = null,
    city: string | null = null,
    contact1: string | null = null,
    contact2: string | null = null,
    contact3: string | null = null,
    remarks: string | null = null) {
    const f: Family = <Family>{};
    f.id = null;
    f.lastUpdate = null;
    f.members = [];
    f.lastName = lastName;
    f.street = street;
    f.postalCode = postalCode;
    f.city = city;
    f.contact1 = contact1;
    f.contact2 = contact2;
    f.contact3 = contact3;
    f.remarks = remarks;
    return f;
  }

  public static copyFamilyValues(source: Family, target: Family): void {
    target.lastName = source.lastName;
    target.street = source.street;
    target.postalCode = source.postalCode;
    target.city = source.city;
    target.contact1 = source.contact1 === null ? null : source.contact1.trim();
    target.contact2 = source.contact2 === null ? null : source.contact2.trim();
    target.contact3 = source.contact3 === null ? null : source.contact3.trim();
    target.remarks = source.remarks;
  }

  public static copyFamilyMembers(source: Family, target: Family): void {
    target.members = source.members;
  }
}

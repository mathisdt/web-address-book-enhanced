export interface Person {
  [index: string]: string | number | null;

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
}

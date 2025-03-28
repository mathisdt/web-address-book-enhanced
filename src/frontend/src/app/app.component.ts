import {Component, inject, OnInit, signal, WritableSignal} from '@angular/core';
import {FamilyComponent} from './family/family.component';
import {DownloadComponent} from './download/download.component';
import {EmailComponent} from './email/email.component';
import {Family} from './family/model/Family';
import {BackendService} from './backend.service';
import {FamilyHelper} from './family/model/FamilyHelper';

@Component({
  selector: 'app-root',
  imports: [FamilyComponent, DownloadComponent, EmailComponent],
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  private readonly backend = inject(BackendService);

  title = 'Web Address Book';
  families: WritableSignal<Family[]> = signal<Family[]>([]);

  createFamily() {
    this.backend.createFamily().subscribe({
      next: created => {
        this.families().push(created);
        console.log(`created family ${created.id}`);
      },
      error: error => console.error(`failed to create family:`, error)
    });
  }

  ngOnInit(): void {
    this.backend.readAllFamilies().subscribe({
      next: result => {
        this.families.set(result)
        console.log(`loaded all ${result.length} families`);
      },
      error: error => console.error(`failed to load all families:`, error)
    });
  }

  familyDeleted(deletedFamily: Family) {
    this.families.set(this.families().filter((f: Family) => f.id !== deletedFamily.id));
  }

  familyChanged(changedFamily: Family) {
    this.families.update(ff => {
      const result: Family[] = [];
      for (const fam of ff) {
        if (fam.id === changedFamily.id) {
          FamilyHelper.copyFamilyValues(changedFamily, fam);
          FamilyHelper.copyFamilyMembers(changedFamily, fam);
        }
        result.push(fam);
      }
      return result;
    });
  }
}

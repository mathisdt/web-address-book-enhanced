import {Component, inject, OnInit, signal, WritableSignal} from '@angular/core';
import {FamilyComponent} from './family-component/family.component';
import {DownloadComponent} from './download-component/download.component';
import {EmailComponent} from './email-component/email.component';
import {Family} from './model/Family';
import {BackendService} from './backend.service';

@Component({
  selector: 'app-root',
  imports: [FamilyComponent, DownloadComponent, EmailComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  backend: BackendService = inject(BackendService);

  title = 'Web Address Book';
  families: WritableSignal<Family[]> = signal<Family[]>([]);

  createFamily() {
    this.backend.createFamily().subscribe(
      created => {
        this.families().push(created);
        console.log(`created family ${created.id}`);
      }
  )
  }

  ngOnInit(): void {
    this.backend.readAllFamilies().subscribe(
      result => {
        this.families.set(result)
        console.log(`loaded all ${result.length} families`);
      }
    )
  }

  familyDeleted(deletedFamily: Family) {
    this.families.set(this.families().filter((f: Family) => f.id !== deletedFamily.id));
  }
}

import { Component, OnInit } from '@angular/core';

type Availability = 'free' | 'taken';

interface Machine {
  id: number;
  name: string;
  status: boolean;
  availability: Availability;
  createdAt: Date;
  userId: number;
}

@Component({
  selector: 'app-search-machines',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  machines: Machine[] = [
    { id: 1, name: 'VM-Alpha', status: true,  availability: 'free', createdAt: new Date('2024-11-01'), userId: 1 },
    { id: 2, name: 'VM-Beta',  status: false, availability: 'taken',  createdAt: new Date('2024-10-25'), userId: 1 },
    { id: 3, name: 'VM-Gamma', status: true,  availability: 'taken',  createdAt: new Date('2024-11-05'), userId: 2 },
    { id: 4, name: 'VM-Delta', status: true,  availability: 'free', createdAt: new Date('2024-11-07'), userId: 1 },
    { id: 5, name: 'VM-Epsilon', status: false, availability: 'free', createdAt: new Date('2024-09-15'), userId: 1 }
  ];

  currentUserId: number = 1;
  
  qName: string = '';
  qStatus: 'all' | 'active' | 'deleted' = 'all';
  qAvailability: 'all' | Availability = 'all';
  qStartDate: string = '';
  qEndDate: string = '';

  results: Machine[] = [];

  constructor() { }

  ngOnInit(): void {
    this.resetAndShowAll();
  }

  private dateToYMD(d: Date): string {
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }

  formatDisplayDate(d: Date): string {
    const day = d.getDate();
    const month = d.getMonth() + 1;
    const year = d.getFullYear();
    return `${day}.${month}.${year}.`;
  }

  search() {
    let list = this.machines.filter(m => m.userId === this.currentUserId);

    const name = this.qName.trim().toLowerCase();
    if (name) {
      list = list.filter(m => m.name.toLowerCase().includes(name));
    }

    if (this.qStatus === 'active') {
      list = list.filter(m => m.status === true);
    } else if (this.qStatus === 'deleted') {
      list = list.filter(m => m.status === false);
    }

    if (this.qAvailability === 'free' || this.qAvailability === 'taken') {
      list = list.filter(m => m.availability === this.qAvailability);
    }
    if (this.qStartDate && this.qEndDate) {
      const start = this.qStartDate;
      const end = this.qEndDate;
      list = list.filter(m => {
        const created = this.dateToYMD(m.createdAt);
        return created >= start && created <= end;
      });
    } else if (this.qStartDate && !this.qEndDate) {
      const start = this.qStartDate;
      list = list.filter(m => this.dateToYMD(m.createdAt) >= start);
    } else if (!this.qStartDate && this.qEndDate) {
      const end = this.qEndDate;
      list = list.filter(m => this.dateToYMD(m.createdAt) <= end);
    }

    this.results = list;
  }
  resetAndShowAll() {
    this.qName = '';
    this.qStatus = 'all';
    this.qAvailability = 'all';
    this.qStartDate = '';
    this.qEndDate = '';
    this.results = this.machines.filter(m => m.userId === this.currentUserId);
  }

}

import {Component, OnInit} from '@angular/core';
import {CommonModule} from "@angular/common";
import {RouterLink, RouterOutlet} from "@angular/router";
import {UsersService} from "../users.service";

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterOutlet
  ],
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})
export class NavComponent implements OnInit{
  isAuthenticated: boolean = false;
  isAdmin: boolean = false;
  isUser: boolean = false;

  constructor(private readonly userService: UsersService) { }

  ngOnInit(): void {
        this.isAuthenticated = this.userService.isAuthenticated();
        this.isAdmin = this.userService.isAdmin();
        this.isUser = this.userService.isUser();
    }

  logout() {
    this.userService.logOut();
    this.isAuthenticated = false;
    this.isAdmin = false;
    this.isUser = false;

  }
}

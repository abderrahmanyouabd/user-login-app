import {Component, inject} from '@angular/core';
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {UsersService} from "../users.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  email: string = '';
  password: string = '';
  errorMessage: string = '';


  constructor( private readonly usersService: UsersService, private router: Router) { }


  async handleSubmit() {
    if (!this.email || !this.password) {
      this.showError("Email and Password is required");
      return;
    }

    try {
      const response = await this.usersService.login(this.email, this.password);
      if(response.statusCode ==  200){
        localStorage.setItem('token', response.token)
        localStorage.setItem('role', response.role)
        await this.router.navigate(['/profile'])
      }else{
        console.log(response)
        this.showError(response.error)
      }
    } catch (error: any) {
      this.showError(error.error)
    }

  }

  showError(mess: string) {
    this.errorMessage = mess;
    setTimeout(() => {
      this.errorMessage = ''
    }, 3000)
  }

}

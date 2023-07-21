export interface FormField{
    date: Date
    description: string
    city: string
    destination: string
    file: File
    googleUser: GoogleUser
}

export interface GoogleUser{
    email: string
}
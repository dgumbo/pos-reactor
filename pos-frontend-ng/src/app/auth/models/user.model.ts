export interface User {
    id?: number;
    
    username: string;
    password?: string;
    fullname?: string;

    activeStatus?: boolean;
    firstName?: string;
    lastName?: string;
    email?: string;
    cancellationAuthoriser?: boolean;
    qualifications?: string;
}

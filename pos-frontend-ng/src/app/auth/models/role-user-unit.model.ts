import {Role} from "./role.model";
import {User} from "./user.model";
import {Unit} from "./unit.model";

export interface RoleUserUnit {
    id?: number;
    role: Role;
    user: User;
    unit: Unit;
}

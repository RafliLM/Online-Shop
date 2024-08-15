import { Customer } from "../customer/customer";
import { Item } from "../item/item";

export class Order {
    constructor(
        public orderId: number | null,
        public orderCode: string,
        public orderDate: Date | null,
        public totalPrice: number,
        public quantity: number,
        public customer: Customer | null,
        public item: Item | null
    ) {}
}
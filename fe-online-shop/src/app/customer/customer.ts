export class Customer {
    constructor(
        public customerId: number | null,
        public customerName: string,
        public customerCode: string,
        public customerPhone: string,
        public customerAddress: string,
        public pic: string | File | ArrayBuffer | null,
        public isActive: boolean,
        public lastOrderDate: Date | null
    ) {}
}
export class Item {
    constructor(
        public itemId: number | null,
        public itemName: string,
        public itemCode: string,
        public stock: number,
        public price: number,
        public isAvailable: boolean,
        public lastReStock: Date | null
    ) {}
}
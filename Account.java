/**
 * บัญชีธนาคาร — ไฟล์ที่นิสิตต้องแก้ (ส่วนที่ 1)
 *
 * ตอนนี้คลาสนี้ ยังไม่ปลอดภัยต่อเธรด
 * balance คือ shared mutable state: หลายเธรดมองเห็นและเขียนทับกันได้
 *
 * อย่าเพิ่งแก้อะไรจนกว่าจะทำ Stage 1 ในไฟล์ README.md เสร็จ
 * ต้องเห็นมันพังด้วยตาตัวเองก่อน แล้วค่อยแก้
 */
public class Account {

    private final int id;

    /** ยอดเงินคงเหลือ — จุดที่เธรดหลายตัวแย่งกันเขียน */
    private int balance;

    /**
     * @param id            เลขบัญชี ต้องไม่ซ้ำกันในระบบเดียวกัน
     * @param initialBalance ยอดตั้งต้น ต้องไม่ติดลบ
     */
    public Account(int id, int initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("initialBalance must not be negative");
        }
        this.id = id;
        this.balance = initialBalance;
    }

    /** เลขบัญชี — ค่านี้ไม่เปลี่ยนตลอดอายุ object จึงไม่ต้องคุ้มครอง */
    public int id() {
        return id;
    }

    // ---------------------------------------------------------------
    // TODO 1.3  อ่านอย่างเดียวก็ต้องคุ้มครอง — แก้แล้ว
    //
    // แม้ไม่ได้เขียนอะไรเลย แต่ balance เป็น field ธรรมดา ไม่มีการรับ
    // ประกัน visibility ข้ามเธรด เธรดที่อ่านอาจเห็นค่าเก่าที่ค้างอยู่
    // ในแคชของ CPU ไม่ใช่ค่าล่าสุดที่อีกเธรดเพิ่งเขียนไป
    // synchronized สร้าง happens-before edge ทำให้มองเห็นค่าล่าสุดเสมอ
    // ---------------------------------------------------------------
    public synchronized int balance() {
        return balance;
    }

    // ---------------------------------------------------------------
    // TODO 1.1  read-modify-write — แก้แล้ว
    //
    // balance = balance + amount; คือ อ่าน → บวก → เขียน สามจังหวะ
    // ใส่ synchronized ให้ทั้งสามจังหวะทำเป็นหน่วยเดียว ห้ามถูกแทรกกลางคัน
    // ---------------------------------------------------------------
    public synchronized void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        balance = balance + amount;
    }

    // ---------------------------------------------------------------
    // TODO 1.2  check-then-act — แก้แล้ว
    //
    // ต้องทำให้ "ตรวจ" กับ "ทำ" เป็นหน่วยเดียวกัน ห้ามมีเธรดอื่นแทรก
    // ระหว่าง if (balance >= amount) กับการหักเงิน มิฉะนั้นยอดจะติดลบ
    // ทั้งที่โค้ดมี if ป้องกันอยู่แล้วก็ตาม
    // ---------------------------------------------------------------
    public synchronized boolean withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (balance >= amount) {
            balance = balance - amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Account#" + id + "(" + balance() + ")";
    }
}
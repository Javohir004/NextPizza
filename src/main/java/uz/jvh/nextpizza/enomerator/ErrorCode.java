package uz.jvh.nextpizza.enomerator;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCode {

    PIZZA_NOT_FOUND("Pizza topilmadi", HttpStatus.NOT_FOUND),
    PIZZA_ALREADY_EXISTS("Bu nomdagi pizza allaqachon mavjud", HttpStatus.CONFLICT),

    DRINK_NOT_FOUND("Ichimlik topilmadi", HttpStatus.NOT_FOUND),
    DRINK_ALREADY_EXISTS("Bu ichimlik allaqachon mavjud", HttpStatus.CONFLICT),



    USER_NOT_FOUND("Foydalanuvchi topilmadi", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("Bu foydalanuvchi allaqachon ro'yxatdan o'tgan", HttpStatus.CONFLICT),


    EMAIL_ALREADY_EXISTS("Bu email allaqachon ro'yxatdan o'tgan", HttpStatus.CONFLICT),
    PHONE_ALREADY_EXISTS("Bu telefon raqam allaqachon ro'yxatdan o'tgan", HttpStatus.CONFLICT),
    INSUFFICIENT_BALANCE("Balans yetarli emas", HttpStatus.BAD_REQUEST),


    // ==================== AUTHENTICATION ====================
    INVALID_CREDENTIALS("Login yoki parol noto'g'ri", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("Token yaroqsiz yoki muddati tugagan", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("Token muddati tugagan", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("Tizimga kirish kerak", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("Bu amalni bajarish uchun huquq yo'q", HttpStatus.FORBIDDEN),


    // ==================== CART ====================
    CART_ALREADY_EXISTS("Userda allaqachon cart yaratilgan" ,  HttpStatus.CONFLICT),
    CART_NOT_FOUND("Savat topilmadi", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND("Savat elementi topilmadi", HttpStatus.NOT_FOUND),
    CART_EMPTY("Savat bo'sh", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY("Miqdor 1 dan kichik bo'lishi mumkin emas", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_STOCK("Mahsulot qolmagan", HttpStatus.BAD_REQUEST),


    // ==================== ORDER ====================
    ORDER_NOT_FOUND("Buyurtma topilmadi", HttpStatus.NOT_FOUND),
    ORDER_ITEM_NOT_FOUND("Buyurtma elementi topilmadi", HttpStatus.NOT_FOUND),
    ORDER_ALREADY_CANCELLED("Buyurtma allaqachon bekor qilingan", HttpStatus.BAD_REQUEST),
    ORDER_CANNOT_BE_CANCELLED("Bu buyurtmani bekor qilib bo'lmaydi", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_COMPLETED("Buyurtma allaqachon yakunlangan", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_STATUS("Noto'g'ri buyurtma holati", HttpStatus.BAD_REQUEST),
    INVALID_STATUS_TRANSITION("Bu holatga o'tish mumkin emas", HttpStatus.BAD_REQUEST),


    // ==================== VALIDATION ====================
    INVALID_INPUT("Kiritilgan ma'lumot noto'g'ri", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD("Majburiy maydon to'ldirilmagan", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT("Email formati noto'g'ri", HttpStatus.BAD_REQUEST),
    INVALID_PHONE_FORMAT("Telefon raqam formati noto'g'ri", HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_SHORT("Parol juda qisqa (kamida 6 ta belgi)", HttpStatus.BAD_REQUEST),


    // ==================== GENERAL ====================
    BAD_REQUEST("Noto'g'ri so'rov", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("Serverda xatolik yuz berdi", HttpStatus.INTERNAL_SERVER_ERROR),
    RESOURCE_NOT_FOUND("Resurs topilmadi", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED("Bu metod ruxsat etilmagan", HttpStatus.METHOD_NOT_ALLOWED);

    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}

package IETI.Lab1User.exception;

import IETI.Lab1User.dto.ServerErrorResponseDto;
import IETI.Lab1User.entities.enums.ErrorCodeEnum;
import org.springframework.http.HttpStatus;

import javax.ws.rs.InternalServerErrorException;

public class InvalidCredentialsException extends InternalServerErrorException
{
    public InvalidCredentialsException() {

        super(new ServerErrorResponseDto("User not found", ErrorCodeEnum.USER_NOT_FOUND, HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND);

    }
    
}

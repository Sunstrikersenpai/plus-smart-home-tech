package ru.yandex.practicum.interaction.exeception;

public class NoOrderFoundException extends RuntimeException {
  public NoOrderFoundException(String message) {
    super(message);
  }
}
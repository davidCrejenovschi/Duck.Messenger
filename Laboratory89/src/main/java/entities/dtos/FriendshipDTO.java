package entities.dtos;

import entities.users.AbstractUser;

public record FriendshipDTO( AbstractUser sender, AbstractUser receiver) { }
CREATE SCHEMA `user` DEFAULT CHARACTER SET utf8 ;

    CREATE TABLE `user`.`user` (
    `id_user` INT NOT NULL AUTO_INCREMENT,
    `login` VARCHAR(60) NOT NULL,
    `password` VARCHAR(40) NOT NULL,
    `score` INT NULL,
    PRIMARY KEY (`id_user`),
    UNIQUE INDEX `login_UNIQUE` (`login` ASC))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


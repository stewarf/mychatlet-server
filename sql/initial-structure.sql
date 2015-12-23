CREATE TABLE `cmlt_charlas` (
  `id_charla` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `id_servidor` int(11) NOT NULL,
  `fecha` datetime NOT NULL,
  `mensaje` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_charla`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `cmlt_servidores` (
  `id_servidor` int(11) NOT NULL AUTO_INCREMENT,
  `hostname` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `puerto` int(11) NOT NULL,
  PRIMARY KEY (`id_servidor`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `cmlt_usuarios` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_usuario`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `cmlt_usuarios_log` (
  `id_log` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) NOT NULL,
  `id_servidor` int(11) NOT NULL,
  `fecha` datetime NOT NULL,
  PRIMARY KEY (`id_log`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `cmlt_usuarios_online` (
  `id_usuario` int(11) NOT NULL,
  `id_servidor` int(11) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0'
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

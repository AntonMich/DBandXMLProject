-- phpMyAdmin SQL Dump
-- version 4.4.15.7
-- http://www.phpmyadmin.net
--
-- Хост: 192.168.1.250:3307
-- Время создания: Мар 02 2017 г., 18:35
-- Версия сервера: 5.5.50
-- Версия PHP: 5.3.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `DepDB`
--

-- --------------------------------------------------------

--
-- Структура таблицы `DepTable`
--
CREATE DATABASE IF NOT EXISTS `DepDB`;
USE `DepDB`;
CREATE TABLE IF NOT EXISTS `DepTable` (
  `Id` int(11) NOT NULL,
  `DepCode` varchar(20) NOT NULL,
  `DepJob` varchar(100) NOT NULL,
  `Description` varchar(255) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `DepTable`
--

INSERT INTO `DepTable` (`Id`, `DepCode`, `DepJob`, `Description`) VALUES
(78, '9210', 'Data manager', 'LICENSE TO USESubject to the terms and conditions of this Agreement includingbut not limited tothe Java Technology Restrictions of the Supplemental License TermsOracle gra  nts you a non-exclusive'),
(79, '05', 'Tester', 'DEFINITIONS"Softwaremeans the   software identified above in binary form that you..'),
(80, '01w5', 'Tester', 'DEFINITIONS"Softwaremeans the   software identified above in binary form that you..'),
(81, '9210', 'Data ma nager', 'LICENSE TO USESubject to the terms and conditions of this Agreement includingbut not limited tothe Java Technology Restrictions of   the Supp emental License TermsOracle gra  nts you a non-exclusive'),
(82, '921sacasa0', 'Data ma nager', 'LICENSE TO USESubject to the terms and conditions of this Agreement includingbut not limited tothe Java Technology Restrictions of  the Supp emental License TermsOracle gra  nts you a non-exclusive'),
(83, '105', 'Tester', 'DEFINITIONS"Softwaremeans the   software identified above in binary form that you..');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `DepTable`
--
ALTER TABLE `DepTable`
  ADD PRIMARY KEY (`Id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `DepTable`
--
ALTER TABLE `DepTable`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=84;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

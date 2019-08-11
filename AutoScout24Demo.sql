-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Aug 11, 2019 at 12:35 PM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `AutoScout24Demo`
--

-- --------------------------------------------------------

--
-- Table structure for table `Car`
--

CREATE TABLE `Car` (
  `id` int(11) NOT NULL,
  `title` text COLLATE latin1_general_ci NOT NULL,
  `fuelId` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `newCar` tinyint(1) NOT NULL,
  `mileage` int(11) DEFAULT NULL,
  `firstRegistration` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Dumping data for table `Car`
--

INSERT INTO `Car` (`id`, `title`, `fuelId`, `price`, `newCar`, `mileage`, `firstRegistration`) VALUES
(2, 'BMW', 2, 90000, 0, 120000, '1996-03-06'),
(12, 'audi', 2, 2333, 0, 3434, '2012-10-02'),
(14, 'fiat', 2, 2333, 0, 3434, '2013-08-01'),
(16, 'ford', 1, 20394230, 1, NULL, NULL),
(17, 'fiat_modifi', 1, 2333, 0, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `Fuel`
--

CREATE TABLE `Fuel` (
  `id` int(11) NOT NULL,
  `fuelType` varchar(255) COLLATE latin1_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Dumping data for table `Fuel`
--

INSERT INTO `Fuel` (`id`, `fueltype`) VALUES
(1, 'diesel'),
(2, 'gasoline');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Car`
--
ALTER TABLE `Car`
  ADD PRIMARY KEY (`id`),
  ADD KEY `Car_Category-Car` (`fuel_id`);

--
-- Indexes for table `Fuel`
--
ALTER TABLE `Fuel`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Car`
--
ALTER TABLE `Car`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `Fuel`
--
ALTER TABLE `Fuel`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Car`
--
ALTER TABLE `Car`
  ADD CONSTRAINT `Car_Category-Car` FOREIGN KEY (`fuel_id`) REFERENCES `Fuel` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 19, 2016 at 03:01 PM
-- Server version: 10.1.13-MariaDB
-- PHP Version: 5.6.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `desta`
--

-- --------------------------------------------------------

--
-- Table structure for table `ads`
--

CREATE TABLE `ads` (
  `id` int(11) NOT NULL,
  `logo` varchar(100) NOT NULL,
  `url` varchar(500) NOT NULL,
  `state` varchar(100) NOT NULL,
  `sponser_company-name` varchar(100) NOT NULL,
  `decription` varchar(50000) NOT NULL,
  `counter` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `firebasetokens`
--

CREATE TABLE `firebasetokens` (
  `id` int(11) NOT NULL,
  `deviceid` varchar(100) NOT NULL,
  `token` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `firebasetokens`
--

INSERT INTO `firebasetokens` (`id`, `deviceid`, `token`) VALUES
(1, '23207dd925a37acd', 'dqE3siuh_Ak:APA91bH5HRFhSvGl5oTQhndn5iqy7ObvMUlsaLcl07t8BBcX6Z1FsRLCZEbaIvLaveIBPcjKDEBDrpgAVipMbQ9337H8qDYvwxgZ9iP5cHYnzR8z8qxVJOO7cszpKLLqOBVUuqJn-G3I');

-- --------------------------------------------------------

--
-- Table structure for table `photodetails`
--

CREATE TABLE `photodetails` (
  `photoId` int(11) NOT NULL,
  `userId` varchar(12) NOT NULL,
  `photoPath` varchar(100) NOT NULL,
  `photoCategory` varchar(100) NOT NULL,
  `post_date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `photodetails`
--

INSERT INTO `photodetails` (`photoId`, `userId`, `photoPath`, `photoCategory`, `post_date`) VALUES
(2, '2', '../images/dysp.jpg', 'Dog', '2016-09-02 23:36:43'),
(3, '4', '../images/img043.jpg', 'Dog', '2016-09-02 23:36:43'),
(4, '5', '../images/P_20160305_190416_1.jpg', 'Dog', '2016-09-02 23:36:43'),
(5, '2', '../images/P_20160305_193623_1.jpg', 'Dog', '2016-09-02 23:36:43'),
(6, '4', '../images/1day.jpg', 'Cat', '2016-09-03 11:59:18'),
(7, '2', '../images/276.jpg', 'Bird', '2016-09-03 11:59:18'),
(8, '4', '../images/277.gif', 'Bird', '2016-09-03 11:59:18'),
(9, '5', '../images/284.jpg', 'Bird', '2016-09-03 11:59:18'),
(10, '4', '../images/10072_413646898730948_353569269_n.jpg', 'Bird', '2016-09-03 11:59:18'),
(11, '2', '../images/8encourage.jpg', 'Cat', '2016-09-03 14:23:41'),
(12, '2', '../images/7446.JPG', 'Cat', '2016-09-03 14:23:41'),
(13, '5', '../images/14173_244477765707258_1019775632_n.jpeg', 'Cat', '2016-09-03 14:23:41'),
(14, '5', '../images/20160906_084845.png', 'Dog', '2016-09-06 08:49:48'),
(24, '1', '../images/Test.png', 'Cat', '2016-09-16 12:29:33'),
(25, '1', '../images/Test.png', 'Dog', '2016-09-16 12:29:33'),
(26, '1', '../images/Test2.png', 'Cat', '2016-09-16 12:29:33'),
(27, '1', '../images/Test2.png', 'Dog', '2016-09-16 12:29:33'),
(28, '8', '../images/Pet.png', 'Dog', '2016-09-19 11:53:28'),
(29, '8', '../images/Pet.png', 'Cat', '2016-09-19 11:53:28');

-- --------------------------------------------------------

--
-- Table structure for table `userdetails`
--

CREATE TABLE `userdetails` (
  `userId` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `state` varchar(100) NOT NULL,
  `mobileNo` varchar(12) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `activationcode` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `userdetails`
--

INSERT INTO `userdetails` (`userId`, `name`, `state`, `mobileNo`, `email`, `password`, `activationcode`) VALUES
(2, 'Pratik Sonawane', 'Maharashtra', '2', 'ptk@gmail.com', 'pratik', ''),
(4, 'Sanket Dhotre', 'Goa', '4', 'srd@gmail.com', 'sanket', ''),
(5, 'Hrishi', 'Bihar', '5', 'hr@gmail.com', 'hrishi', ''),
(8, 'Sanket', 'Maharashtra', '8097375414', 'sankket@gmail.com', 'sanku', ''),
(9, 'amit', 'MP', '22', 'amit@gmail.com', '', ''),
(10, 'ameya', 'Assam', '', 'ameya@gmail.com', 'ameya', ''),
(11, 'rajni', 'Chandigarh', '8888128888', 'google@rajni.com', 'rajni', '');

-- --------------------------------------------------------

--
-- Table structure for table `votedetails`
--

CREATE TABLE `votedetails` (
  `voteId` int(11) NOT NULL,
  `photoId` int(11) NOT NULL,
  `photoCategory` varchar(100) NOT NULL,
  `state` varchar(100) NOT NULL,
  `userId` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `votedetails`
--

INSERT INTO `votedetails` (`voteId`, `photoId`, `photoCategory`, `state`, `userId`) VALUES
(3, 2, 'Dog', 'Maharashtra', '2'),
(4, 11, 'Cat', 'Maharashtra', '2'),
(5, 11, 'Cat', 'Goa', '4'),
(6, 2, 'Dog', 'Maharashtra', '4'),
(7, 2, 'Dog', 'Goa', '5');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ads`
--
ALTER TABLE `ads`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `firebasetokens`
--
ALTER TABLE `firebasetokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `token` (`token`),
  ADD UNIQUE KEY `deviceid` (`deviceid`),
  ADD UNIQUE KEY `token_2` (`token`);

--
-- Indexes for table `photodetails`
--
ALTER TABLE `photodetails`
  ADD PRIMARY KEY (`photoId`);

--
-- Indexes for table `userdetails`
--
ALTER TABLE `userdetails`
  ADD PRIMARY KEY (`userId`),
  ADD UNIQUE KEY `mobileNo` (`mobileNo`);

--
-- Indexes for table `votedetails`
--
ALTER TABLE `votedetails`
  ADD PRIMARY KEY (`voteId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ads`
--
ALTER TABLE `ads`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `firebasetokens`
--
ALTER TABLE `firebasetokens`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `photodetails`
--
ALTER TABLE `photodetails`
  MODIFY `photoId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;
--
-- AUTO_INCREMENT for table `userdetails`
--
ALTER TABLE `userdetails`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `votedetails`
--
ALTER TABLE `votedetails`
  MODIFY `voteId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

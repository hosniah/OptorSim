-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Mer 05 Octobre 2016 à 19:51
-- Version du serveur :  10.1.16-MariaDB
-- Version de PHP :  7.0.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `trias`
--

-- --------------------------------------------------------

--
-- Structure de la table `allvisitedsites`
--

CREATE TABLE `allvisitedsites` (
  `id` int(11) NOT NULL,
  `label` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Index pour les tables exportées
--

--
-- Index pour la table `allvisitedsites`
--
ALTER TABLE `allvisitedsites`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `allvisitedsites`
--
ALTER TABLE `allvisitedsites`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;





--
-- Structure de la table `bgrt`
--

CREATE TABLE `bgrt` (
  `id` int(11) NOT NULL,
  `premisse` varchar(512) NOT NULL,
  `conclusion` varchar(512) NOT NULL,
  `supp_c` float NOT NULL,
  `conf_c` float NOT NULL,
  `tasks_count` int(11) NOT NULL,
  `sites_count` int(11) NOT NULL,
  `rt_sites` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `gridfiles`
--

CREATE TABLE `gridfiles` (
  `id` int(11) NOT NULL,
  `label` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `gridsites`
--

CREATE TABLE `gridsites` (
  `id` int(11) NOT NULL,
  `label` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `gridtasks`
--

CREATE TABLE `gridtasks` (
  `id` int(11) NOT NULL,
  `label` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `triconcepts`
--

CREATE TABLE `triconcepts` (
  `id` int(11) NOT NULL,
  `id_task` int(11) NOT NULL,
  `id_file` int(11) NOT NULL,
  `id_site` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Index pour les tables exportées
--

--
-- Index pour la table `bgrt`
--
ALTER TABLE `bgrt`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_index` (`premisse`,`conclusion`);

--
-- Index pour la table `gridfiles`
--
ALTER TABLE `gridfiles`
  ADD PRIMARY KEY (`id`),
  ADD KEY `label` (`label`) USING BTREE;

--
-- Index pour la table `gridsites`
--
ALTER TABLE `gridsites`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `label` (`label`);

--
-- Index pour la table `gridtasks`
--
ALTER TABLE `gridtasks`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `label` (`label`);

--
-- Index pour la table `triconcepts`
--
ALTER TABLE `triconcepts`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `bgrt`
--
ALTER TABLE `bgrt`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `gridfiles`
--
ALTER TABLE `gridfiles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `gridsites`
--
ALTER TABLE `gridsites`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `gridtasks`
--
ALTER TABLE `gridtasks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `triconcepts`
--
ALTER TABLE `triconcepts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

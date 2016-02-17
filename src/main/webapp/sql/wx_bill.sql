CREATE TABLE IF NOT EXISTS `wx_bill` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `tradetime` varchar(20) NOT NULL,
  `ghid` varchar(20) NOT NULL,
  `mchid` varchar(20) NOT NULL,
  `submch` varchar(10) NOT NULL,
  `deviceid` varchar(16) NOT NULL,
  `wxorder` varchar(30) NOT NULL,
  `bzorder` varchar(30) NOT NULL,
  `openid` varchar(30) NOT NULL,
  `tradetype` varchar(10) NOT NULL,
  `tradestatus` varchar(10) NOT NULL,
  `bank` varchar(16) NOT NULL,
  `currency` varchar(20) NOT NULL,
  `totalmoney` varchar(20) NOT NULL,
  `redpacketmoney` varchar(20) NOT NULL,
  `wxrefund` varchar(10) NOT NULL,
  `bzrefund` varchar(10) NOT NULL,
  `refundmoney` varchar(20) NOT NULL,
  `redpacketrefund` varchar(20) NOT NULL,
  `refundtype` varchar(10) NOT NULL,
  `refundstatus` varchar(10) NOT NULL,
  `productname` varchar(30) NOT NULL,
  `bzdatapacket` varchar(10) NOT NULL,
  `fee` varchar(10) NOT NULL,
  `rate` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `wxorder` (`wxorder`),
  UNIQUE KEY `bzorder` (`bzorder`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=272 ;
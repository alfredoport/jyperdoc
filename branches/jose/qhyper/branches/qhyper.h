#ifndef QHYPER_H
#define QHYPER_H

#include <QtGui/QMainWindow>
#include "ui_qhyper.h"

class qhyper : public QMainWindow
{
    Q_OBJECT

public:
    qhyper(QWidget *parent = 0);
    ~qhyper();

private:
    Ui::qhyperClass ui;
};

#endif // QHYPER_H
